
import api from "./api.js";

//запросит с сервера список voice ранее озвученных по тексту похожих
export const getListCards = async ({ text }) => {
    try {
        const response = await api.post(
            `/api/card/list_cards`,
            { text },
            { headers: { 'Content-Type': 'application/json' } });
        return response.data;
    } catch (e) {
        return Promise.reject(e);
    }
};