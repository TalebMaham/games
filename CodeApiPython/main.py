from fastapi import FastAPI, HTTPException
from recommendation import generate_recommendations
from models import UserData

app = FastAPI()

@app.get("/")
async def root():
    return {"message": "API de recommandation en ligne"}

@app.post("/recommendations/")
async def get_recommendations(data: UserData):
    try:
        recommendations = generate_recommendations(data)
        return {"recommendations": recommendations}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
