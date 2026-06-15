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

        <style>

            body {
                margin:0;
                background:black;
                overflow:hidden;
            }

            img {
                width:100vw;
                height:100vh;
                object-fit:contain;
            }

        </style>

    </head>

    <body>

        <img id="ad">

        <script>

            const ads = [
                "/media/demo2.png",
                "/media/demo3.png",
                "/media/demo4.png"
                "/media/newdemo1.png",
                "/media/newdemo2.png",
            ];

            let index = 0;

            const image = document.getElementById("ad");

            function showAd() {

                image.src =
                    ads[index] + "?t=" + Date.now();

                index =
                    (index + 1) % ads.length;
            }

            showAd();

            setInterval(showAd,3000);

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