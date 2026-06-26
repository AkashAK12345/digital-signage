import { useCallback, useMemo, useState } from "react";
import { Box } from "@mui/material";
import { useSnackbar } from "notistack";

import MediaPageHero from "../../components/media/MediaPageHero";
import MediaStatsRow from "../../components/media/MediaStatsRow";
import MediaFiltersBar from "../../components/media/MediaFiltersBar";
import MediaGrid from "../../components/media/MediaGrid";
import UploadMediaDialog from "../../components/media/UploadMediaDialog";
import MediaPreviewDialog from "../../components/media/MediaPreviewDialog";
import ConfirmDialog from "../../components/common/ConfirmDialog";

import { mediaItems as seedMedia } from "../../data/media";
import { formatBytes, hasActiveFilters, sortMedia } from "../../components/media/utils";

import type { MediaItem } from "../../types/media";
import type { TypeFilter, CategoryFilter, SortField, SortDirection } from "../../components/media/types";

// ---------------------------------------------------------------------------
// MediaPage — orchestrates state and composes reusable media components.
// ---------------------------------------------------------------------------

export default function MediaPage() {
  const { enqueueSnackbar } = useSnackbar();

  // ── Data State ───────────────────────────────────────────────────────────
  const [items, setItems] = useState<MediaItem[]>(seedMedia);
  const [refreshing, setRefreshing] = useState(false);

  // ── Filter & Sort State ──────────────────────────────────────────────────
  const [search, setSearch] = useState("");
  const [typeFilter, setTypeFilter] = useState<TypeFilter>("All");
  const [categoryFilter, setCategoryFilter] = useState<CategoryFilter>("All");
  const [sortField, setSortField] = useState<SortField>("uploadedAt");
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");

  // ── Dialog State ─────────────────────────────────────────────────────────
  const [uploadOpen, setUploadOpen] = useState(false);
  const [previewTarget, setPreviewTarget] = useState<MediaItem | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<MediaItem | null>(null);

  // ── Derived Data ─────────────────────────────────────────────────────────
  const imagesCount = useMemo(() => items.filter(i => i.type === "Image").length, [items]);
  const videosCount = useMemo(() => items.filter(i => i.type === "Video").length, [items]);
  const totalSizeBytes = useMemo(() => items.reduce((acc, i) => acc + i.size, 0), [items]);
  const totalSizeFormatted = formatBytes(totalSizeBytes);

  const filtersActive = hasActiveFilters(search, typeFilter, categoryFilter);

  const filteredItems = useMemo(() => {
    let result = items;

    if (search.trim()) {
      const q = search.trim().toLowerCase();
      result = result.filter(i => i.name.toLowerCase().includes(q));
    }

    if (typeFilter !== "All") {
      result = result.filter(i => i.type === typeFilter);
    }

    if (categoryFilter !== "All") {
      result = result.filter(i => i.category === categoryFilter);
    }

    return sortMedia(result, sortField, sortDirection);
  }, [items, search, typeFilter, categoryFilter, sortField, sortDirection]);

  // ── Handlers ─────────────────────────────────────────────────────────────
  const handleRefresh = useCallback(() => {
    setRefreshing(true);
    setTimeout(() => {
      setRefreshing(false);
      enqueueSnackbar("Media library refreshed", { variant: "success" });
    }, 800);
  }, [enqueueSnackbar]);

  const handleClearFilters = useCallback(() => {
    setSearch("");
    setTypeFilter("All");
    setCategoryFilter("All");
  }, []);

  const handleStatClick = useCallback((filter: TypeFilter) => {
    setTypeFilter(filter);
    setSearch("");
    setCategoryFilter("All");
  }, []);

  // ── Upload Handlers ──────────────────────────────────────────────────────
  const handleUploadSubmit = useCallback((files: File[]) => {
    // Mock upload process
    const newItems: MediaItem[] = files.map((file, i) => {
      const isVideo = file.type.startsWith("video/");
      const mockUrl = URL.createObjectURL(file);
      return {
        id: `MED-NEW-${Date.now()}-${i}`,
        name: file.name,
        type: isVideo ? "Video" : "Image",
        category: "Announcement", // Default mock category
        thumbnail: mockUrl,
        originalFile: mockUrl,
        size: file.size,
        duration: isVideo ? 120 : undefined, // Mock duration
        uploadedAt: Date.now(),
        uploadedBy: "Current User",
        dimensions: "1920x1080", // Mock dimensions
      };
    });

    setItems(prev => [...newItems, ...prev]);
    enqueueSnackbar(`Successfully uploaded ${files.length} file${files.length !== 1 ? 's' : ''}`, { variant: "success" });
    setUploadOpen(false);
  }, [enqueueSnackbar]);

  // ── Delete Handlers ──────────────────────────────────────────────────────
  const handleConfirmDelete = useCallback(() => {
    if (!deleteTarget) return;
    setItems(prev => prev.filter(i => i.id !== deleteTarget.id));
    enqueueSnackbar(`Deleted "${deleteTarget.name}"`, { variant: "success" });
    setDeleteTarget(null);
  }, [deleteTarget, enqueueSnackbar]);

  return (
    <Box>
      <MediaPageHero
        totalFiles={items.length}
        totalSizeFormatted={totalSizeFormatted}
        onUpload={() => setUploadOpen(true)}
        onRefresh={handleRefresh}
        refreshing={refreshing}
      />

      <MediaStatsRow
        totalFiles={items.length}
        totalSizeFormatted={totalSizeFormatted}
        imagesCount={imagesCount}
        videosCount={videosCount}
        onStatClick={handleStatClick}
      />

      <MediaFiltersBar
        search={search}
        typeFilter={typeFilter}
        categoryFilter={categoryFilter}
        sortField={sortField}
        sortDirection={sortDirection}
        resultCount={filteredItems.length}
        hasActiveFilters={filtersActive}
        refreshing={refreshing}
        onSearchChange={setSearch}
        onTypeFilterChange={setTypeFilter}
        onCategoryFilterChange={setCategoryFilter}
        onSortChange={(field, dir) => { setSortField(field); setSortDirection(dir); }}
        onClearFilters={handleClearFilters}
        onRefresh={handleRefresh}
      />

      <MediaGrid
        items={filteredItems}
        hasActiveFilters={filtersActive}
        onPreview={setPreviewTarget}
        onDelete={setDeleteTarget}
        onClearFilters={handleClearFilters}
        onUpload={() => setUploadOpen(true)}
      />

      <UploadMediaDialog
        open={uploadOpen}
        onClose={() => setUploadOpen(false)}
        onSubmit={handleUploadSubmit}
      />

      <MediaPreviewDialog
        item={previewTarget}
        open={!!previewTarget}
        onClose={() => setPreviewTarget(null)}
      />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Delete Media"
        message={deleteTarget ? `Are you sure you want to delete "${deleteTarget.name}"? This file will be permanently removed.` : ""}
        onConfirm={handleConfirmDelete}
        onClose={() => setDeleteTarget(null)}
      />
    </Box>
  );
}