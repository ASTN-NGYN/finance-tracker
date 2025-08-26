"use client"
import { useEffect, useState } from "react";
import { DollarSign, TrendingDown, BarChart3, PiggyBank } from 'lucide-react';
import { MetricCard } from './components/dashboard/metric-card';
import { RecentTransactionsTable } from './components/dashboard/recent-transactions-table';
import { CategoriesCard } from "./components/dashboard/categories-card";
import { getTotalExpenses, getTotalIncome, getTotalSavings } from "./utils/api";

export default function Home() {
  const [totalIncome, setTotalIncome] = useState<number>(0);
  const [totalExpenses, setTotalExpenses] = useState<number>(0);
  const [totalSavings, setTotalSavings] = useState<number>(0);

  const MONTHLY_BUDGET = 1000;

  useEffect(() => {
    async function fetchTotals() {
      const income = await getTotalIncome();
      setTotalIncome(income);
    }
    fetchTotals();
  }, []);

  useEffect(() => {
    async function fetchExpenses() {
      const expenses = await getTotalExpenses();
      setTotalExpenses(expenses);
    }
    fetchExpenses();
  }, []);

  useEffect(() => {
    async function fetchSavings() {
      const savings = await getTotalSavings();
      setTotalSavings(savings);
    }
    fetchSavings();
  }, []);

  const monthlyBudgetLeft = MONTHLY_BUDGET - totalExpenses;

  return (
    <main>
      <MetricCard
        title="Total Income"
        value={`$${totalIncome.toFixed(2)}`}
        icon={DollarSign}
        color="#10b981"
      />
      <MetricCard
        title="Total Expenses"
        value={`$${totalExpenses.toFixed(2)}`}
        icon={TrendingDown}
        color="#ff0000"
      />
      <MetricCard
        title="Monthly Budget Left"
        value={`$${monthlyBudgetLeft.toFixed(2)} / ${MONTHLY_BUDGET.toFixed(2)}`}
        icon={BarChart3}
        color="#197dff"
      />
      <MetricCard
        title="Total Savings"
        value={`$${totalSavings.toFixed(2)}`}
        icon={PiggyBank}
        color="#ff00aa"
      />
      <RecentTransactionsTable />
      <CategoriesCard />
    </main>
  );
}
