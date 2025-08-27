import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 5000,
});

export interface TransactionWithCategoryDTO {
    id: number;
    amount: number;
    description: string;
    date: string;
    type: string;
    categoryName: string;
}

export interface TransactionDTO {
    amount: number;
    description: string;
    date: string;
    categoryId: number;
}

export interface CategoryDTO {
    id: number;
    name: string;
    description: string;
    type: string;
}

export async function getTotalIncome(): Promise<number> {
    try {
        const res = await api.get("/transactions/total-income");
        return res.data || 0;
    } catch (err) {
        console.error("Error fetching total income: ", err);
        return 0;
    }
}

export async function getTotalExpenses(): Promise<number> {
    try {
        const res = await api.get("/transactions/total-expenses");
        return res.data || 0;
    } catch (err) {
        console.error("Error fetching total expenses: ", err);
        return 0;
    }
}

export async function getTotalSavings(): Promise<number> {
    try {
        const res = await api.get("/transactions/total-savings");
        return res.data || 0;
    } catch (err) {
        console.error("Error fetching total savings: ", err);
        return 0;
    }
}

export async function getTransactionWithCategory(): Promise<TransactionWithCategoryDTO[]> {
    try {
        const res = await api.get("/transactions/with-categories");
        return res.data;
    } catch (err) {
        console.error("Error fetching transactions with categories:", err);
        return [];
    }
}

export async function getCategories(): Promise<CategoryDTO[]> {
    try {
        const res = await api.get("/categories");
        return res.data;
    } catch (err) {
        console.error("Error fetching all categories:", err);
        return [];
    }
}


export async function createTransaction(transactions: TransactionDTO) {
    try {
        const res = await api.post("/transactions", transactions)
        return res.data
    } catch (err) {
        console.error("Error creating transactions:", err)
        throw err
    }
}

export default api;