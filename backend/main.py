from fastapi import FastAPI, UploadFile, File
from fastapi.staticfiles import StaticFiles
import shutil
import os
from fastapi.responses import HTMLResponse

app = FastAPI(
    title="Digital Signage API"
)

MEDIA_FOLDER = "media"

os.makedirs(MEDIA_FOLDER, exist_ok=True)

app.mount("/media", StaticFiles(directory="media"), name="media")

current_ad = {
    "image": None
}

@app.get("/")
def home():
    return {
        "message": "Digital Signage Backend Running"
    }


@app.get("/display", response_class=HTMLResponse)
def display_page():

    return """
    <html>

    <head>

        <title>Digital Signage</title>

        <style>

            body {
                margin: 0;
                background: black;
                overflow: hidden;
            }

            img {
                width: 100vw;
                height: 100vh;
                object-fit: contain;
            }

        </style>

    </head>

    <body>

        <img id="ad">

        <script>

            let ads = [];
            let current = 0;

            async function loadAds() {

                const response =
                    await fetch("/ads");

                const data =
                    await response.json();

                ads = data.ads;

                if (ads.length > 0) {
                    showAd();
                }
            }

            function showAd() {

                const img =
                    document.getElementById("ad");

                img.src =
                    ads[current] + "?t=" + Date.now();

                current =
                    (current + 1) % ads.length;
            }

            setInterval(() => {

                if (ads.length > 0) {
                    showAd();
                }

            }, 3000);

            loadAds();

            setInterval(loadAds, 30000);

        </script>

    </body>

    </html>
    """


@app.post("/upload")
async def upload_image(file: UploadFile = File(...)):
    
    file_path = f"{MEDIA_FOLDER}/{file.filename}"

    with open(file_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    current_ad["image"] = file.filename

    return {
        "message": "Uploaded successfully",
        "file": file.filename
    }


@app.get("/current-ad")
def get_current_ad():
    return current_ad

@app.get("/ads")
def get_ads():

    files = []

    for file in os.listdir(MEDIA_FOLDER):

        if file.lower().endswith(
            (".png", ".jpg", ".jpeg", ".webp")
        ):
            files.append(f"/media/{file}")

    return {
        "ads": files
    }