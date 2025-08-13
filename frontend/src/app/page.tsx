import { MetricCard } from '@/app/components/dashboard/metric-card'
import { DollarSign, TrendingDown, BarChart3, PiggyBank } from 'lucide-react';

export default function Home() {
  return (
    <main>
      <MetricCard
        title="Total Income"
        value="$5,240.00"
        icon={DollarSign}
        color="#10b981"
      />
      <MetricCard
        title="Total Expenses"
        value="$143.50"
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
    </main>
  );
}
