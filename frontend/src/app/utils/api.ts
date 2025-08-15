import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 5000,
});

export async function getTotalIncome(): Promise<number> {
    try {
        const res = await api.get("/transactions/total-income");
        return res.data || 0;
    } catch (err) {
        console.error("Error fetching total income:", err);
        return 0;
    }
}

export async function getTotalExpenses(): Promise<number> {
    try {
        const res = await api.get("/transactions/total-expenses");
        return res.data || 0;
    } catch (err) {
        console.error("Error fetching total expenses:", err);
        return 0;
    }
}

export default api;