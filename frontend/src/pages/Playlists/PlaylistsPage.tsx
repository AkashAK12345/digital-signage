import { useState, useMemo, useCallback } from "react";
import { Box } from "@mui/material";
import { useSnackbar } from "notistack";

import PlaylistPageHero from "../../components/playlists/PlaylistPageHero";
import PlaylistStatsRow from "../../components/playlists/PlaylistStatsRow";
import PlaylistFiltersBar from "../../components/playlists/PlaylistFiltersBar";
import PlaylistGrid from "../../components/playlists/PlaylistGrid";
import PlaylistEditor from "../../components/playlists/PlaylistEditor";
import PlaylistPreviewDialog from "../../components/playlists/PlaylistPreviewDialog";
import AssignDevicesDialog from "../../components/playlists/AssignDevicesDialog";
import ConfirmDialog from "../../components/common/ConfirmDialog";

import { playlists as seedPlaylists } from "../../data/playlists";
import { mediaItems } from "../../data/media";
import { hasActiveFilters, sortPlaylists } from "../../components/playlists/utils";

import type { Playlist } from "../../types/playlist";
import type { StatusFilter, SortField, SortDirection } from "../../components/playlists/types";

export default function PlaylistsPage() {
  const { enqueueSnackbar } = useSnackbar();

  // ── Data State ───────────────────────────────────────────────────────────
  const [items, setItems] = useState<Playlist[]>(seedPlaylists);
  const [refreshing, setRefreshing] = useState(false);

  // ── Filter & Sort State ──────────────────────────────────────────────────
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState<StatusFilter>("All");
  const [sortField, setSortField] = useState<SortField>("updatedAt");
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");

  // ── Dialog / Editor State ────────────────────────────────────────────────
  const [editorOpen, setEditorOpen] = useState(false);
  const [editorTarget, setEditorTarget] = useState<Playlist | undefined>(undefined);
  const [previewTarget, setPreviewTarget] = useState<Playlist | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Playlist | null>(null);
  const [assignTarget, setAssignTarget] = useState<Playlist | null>(null);

  // ── Derived Data ─────────────────────────────────────────────────────────
  const publishedCount = useMemo(() => items.filter(i => i.status === "Published").length, [items]);
  const draftCount = useMemo(() => items.filter(i => i.status === "Draft").length, [items]);
  const archivedCount = useMemo(() => items.filter(i => i.status === "Archived").length, [items]);

  const filtersActive = hasActiveFilters(search, statusFilter);

  const filteredItems = useMemo(() => {
    let result = items;
    if (search.trim()) {
      const q = search.trim().toLowerCase();
      result = result.filter(i => i.name.toLowerCase().includes(q));
    }
    if (statusFilter !== "All") {
      result = result.filter(i => i.status === statusFilter);
    }
    return sortPlaylists(result, sortField, sortDirection);
  }, [items, search, statusFilter, sortField, sortDirection]);

  // ── Handlers ─────────────────────────────────────────────────────────────
  const handleRefresh = useCallback(() => {
    setRefreshing(true);
    setTimeout(() => {
      setRefreshing(false);
      enqueueSnackbar("Playlists refreshed", { variant: "success" });
    }, 800);
  }, [enqueueSnackbar]);

  const handleClearFilters = useCallback(() => {
    setSearch("");
    setStatusFilter("All");
  }, []);

  const handleStatClick = useCallback((filter: StatusFilter) => {
    setStatusFilter(filter);
    setSearch("");
  }, []);

  // ── Editor Handlers ──────────────────────────────────────────────────────
  const handleCreate = useCallback(() => {
    setEditorTarget(undefined);
    setEditorOpen(true);
  }, []);

  const handleEdit = useCallback((playlist: Playlist) => {
    setEditorTarget(playlist);
    setEditorOpen(true);
  }, []);

  const handleSaveEditor = useCallback((playlist: Playlist) => {
    setItems(prev => {
      const idx = prev.findIndex(p => p.id === playlist.id);
      if (idx >= 0) {
        const copy = [...prev];
        copy[idx] = playlist;
        return copy;
      }
      return [playlist, ...prev];
    });
    enqueueSnackbar(`Playlist "${playlist.name}" saved successfully`, { variant: "success" });
    setEditorOpen(false);
  }, [enqueueSnackbar]);

  // ── Delete Handlers ──────────────────────────────────────────────────────
  const handleConfirmDelete = useCallback(() => {
    if (!deleteTarget) return;
    setItems(prev => prev.filter(i => i.id !== deleteTarget.id));
    enqueueSnackbar(`Deleted "${deleteTarget.name}"`, { variant: "success" });
    setDeleteTarget(null);
  }, [deleteTarget, enqueueSnackbar]);

  // ── Assign Handlers ──────────────────────────────────────────────────────
  const handleSaveAssignments = useCallback((deviceIds: string[]) => {
    if (!assignTarget) return;
    setItems(prev => {
      const copy = [...prev];
      const idx = copy.findIndex(p => p.id === assignTarget.id);
      if (idx >= 0) copy[idx] = { ...copy[idx], assignedDeviceIds: deviceIds, updatedAt: Date.now() };
      return copy;
    });
    enqueueSnackbar(`Updated device assignments for "${assignTarget.name}"`, { variant: "success" });
    setAssignTarget(null);
  }, [assignTarget, enqueueSnackbar]);

  return (
    <Box>
      <PlaylistPageHero
        totalPlaylists={items.length}
        onCreate={handleCreate}
        onRefresh={handleRefresh}
        refreshing={refreshing}
      />

      <PlaylistStatsRow
        total={items.length}
        published={publishedCount}
        draft={draftCount}
        archived={archivedCount}
        onStatClick={handleStatClick}
      />

      <PlaylistFiltersBar
        search={search}
        statusFilter={statusFilter}
        sortField={sortField}
        sortDirection={sortDirection}
        resultCount={filteredItems.length}
        refreshing={refreshing}
        onSearchChange={setSearch}
        onStatusFilterChange={setStatusFilter}
        onSortChange={(field, dir) => { setSortField(field); setSortDirection(dir); }}
        onClearFilters={handleClearFilters}
        onRefresh={handleRefresh}
      />

      <PlaylistGrid
        playlists={filteredItems}
        hasActiveFilters={filtersActive}
        onEdit={handleEdit}
        onPreview={setPreviewTarget}
        onDelete={setDeleteTarget}
        onClearFilters={handleClearFilters}
        onCreate={handleCreate}
      />

      {/* Overlays */}
      <PlaylistEditor
        open={editorOpen}
        initialPlaylist={editorTarget}
        mediaLibrary={mediaItems}
        onClose={() => setEditorOpen(false)}
        onSave={handleSaveEditor}
      />

      <PlaylistPreviewDialog
        open={!!previewTarget}
        items={previewTarget?.items || []}
        mediaLibrary={mediaItems}
        onClose={() => setPreviewTarget(null)}
      />

      <AssignDevicesDialog
        open={!!assignTarget}
        initialAssignedIds={assignTarget?.assignedDeviceIds || []}
        onClose={() => setAssignTarget(null)}
        onSave={handleSaveAssignments}
      />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Delete Playlist"
        message={deleteTarget ? `Are you sure you want to delete "${deleteTarget.name}"? This will not delete the underlying media files.` : ""}
        onConfirm={handleConfirmDelete}
        onClose={() => setDeleteTarget(null)}
      />
    </Box>
  );
}