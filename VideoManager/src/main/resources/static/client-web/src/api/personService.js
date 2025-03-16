import api from "./api.js";

//обновит рейтинг голосов
//для простоты на сервере помещен в один обьект Person
//так как он привязан к конкретному переводчику
export const updateRatingVoices = async ({ newRatingVoices, oldRatingVoices }) => {
    try {
        await api.post('/api/person/rating_voices', newRatingVoices, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return newRatingVoices;
    } catch (error) {
        console.error('Error updating rating:', error);
        return Promise.reject(oldRatingVoices, error);
    }
};

export const getRatingVoices = async () => {
    try {
        const responce = await api.get(`/api/person/rating_get`);
        return responce.data;
    } catch (error){
        return Promise.reject(error);
    }
}