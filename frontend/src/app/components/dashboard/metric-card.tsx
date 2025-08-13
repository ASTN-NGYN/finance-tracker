import { Card, CardHeader, CardTitle, CardContent, CardAction, CardFooter } from "@/components/ui/card";
import { LucideIcon } from "lucide-react";


type MetricCardProps = {
  title: string;
  value: string;
  icon: React.ElementType;
  color?: string;
};

export function MetricCard({ title, value, icon: Icon, color = "#3b82f6" }: MetricCardProps) {
  return (
    <Card className="hover:shadow-md transition-shadow mb-6">
      <CardContent className="flex items-center justify-between p-6">
        <div>
          <p className="text-md font-medium text-muted-foreground">{title}</p>
          <p className="text-3xl font-bold">{value}</p>
        </div>
        <div
          className="p-3 rounded-full"
          style={{ backgroundColor: color + "20" }}
        >
          <Icon className="w-7 h-7" style={{ color }} />
        </div>
      </CardContent>
    </Card>
  );
}