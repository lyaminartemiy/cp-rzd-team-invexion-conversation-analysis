import uvicorn
from fastapi import FastAPI

from src.api.routers.analysis import router as analysis_router
from src.api.routers.speech2text import router as speech2text_router


app = FastAPI()

app.include_router(analysis_router)
app.include_router(speech2text_router)


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
