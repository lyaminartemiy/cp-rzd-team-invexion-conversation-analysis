from fastapi import APIRouter

from src.schemas.speech2text import Speech2TextAnswer
from src.services.controllers.speech2text import (
    get_speech2text_pipeline,
)


router = APIRouter()


@router.get("/speech2text/")
async def get_speech2text(
    uuid: str
) -> Speech2TextAnswer:
    """
    Endpoint to retrieve the result of the speech-to-text pipeline.

    Parameters:
    -----------
    uuid : str
        The unique identifier of the speech file.

    Returns:
    --------
    Speech2TextAnswer
        An object containing the decoded text.
    """
    return await get_speech2text_pipeline(uuid)
