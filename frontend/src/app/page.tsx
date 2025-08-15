"use client"
import { useEffect, useState } from "react";
import { DollarSign, TrendingDown, BarChart3, PiggyBank } from 'lucide-react';
import { MetricCard } from '@/app/components/dashboard/metric-card';
import { RecentTransactionsTable } from './components/dashboard/recent-transactions-table';
import { getTotalExpenses, getTotalIncome } from "./utils/api";

export default function Home() {
  const [totalIncome, setTotalIncome] = useState<number>(0);
  const [totalExpenses, setTotalExpenses] = useState<number>(0);
  
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

  // const monthlyBudgetLeft = 500 - totalExpenses;

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
        value="$300.00/500.00"
        icon={BarChart3}
        color="#197dff"
      />
      <MetricCard
        title="Total Savings"
        value="$6500.00"
        icon={PiggyBank}
        color="#ff00aa"
      />
      <RecentTransactionsTable />
    </main>
  );
}
