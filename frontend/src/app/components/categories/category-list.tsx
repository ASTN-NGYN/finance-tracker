"use client";

import { useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import {
  Trash2,
  Home,
  Car,
  Utensils,
  Lightbulb,
  ShoppingBag,
  Heart,
  Film,
  MoreHorizontal,
} from "lucide-react";

export default function CategoryList() {
  const [categories, setCategories] = useState([
    { id: 1, name: "Housing", icon: <Home className="w-4 h-4" /> },
    { id: 2, name: "Transport", icon: <Car className="w-4 h-4" /> },
    { id: 3, name: "Food", icon: <Utensils className="w-4 h-4" /> },
    { id: 4, name: "Utilities", icon: <Lightbulb className="w-4 h-4" /> },
    { id: 5, name: "Shopping", icon: <ShoppingBag className="w-4 h-4" /> },
    { id: 6, name: "Health", icon: <Heart className="w-4 h-4" /> },
    { id: 7, name: "Entertainment", icon: <Film className="w-4 h-4" /> },
    { id: 8, name: "Other", icon: <MoreHorizontal className="w-4 h-4" /> },
  ]);

  const [newCategory, setNewCategory] = useState("");

  const handleAdd = () => {
    if (newCategory.trim() !== "") {
      setCategories([
        ...categories,
        {
          id: Date.now(),
          name: newCategory,
          icon: <MoreHorizontal className="w-4 h-4" />,
        },
      ]);
      setNewCategory("");
    }
  };

  const handleDelete = (id: number) => {
    setCategories(categories.filter((cat) => cat.id !== id));
  };

  return (
    <div className="mt-10">
      <ScrollArea className="h-[400px] rounded-md border p-4 mb-6">
        <div className="grid grid-cols-2 gap-4">
          {categories.map((cat) => (
            <Card key={cat.id} className="flex items-center justify-between p-3">
              <CardContent className="flex items-center gap-2 p-0">
                {cat.icon}
                <span>{cat.name}</span>
              </CardContent>
              <Button
                variant="ghost"
                size="icon"
                onClick={() => handleDelete(cat.id)}
              >
                <Trash2 className="w-4 h-4 text-red-500" />
              </Button>
            </Card>
          ))}
        </div>
      </ScrollArea>

        <div className="flex justify-center">
            <div className="flex flex-col gap-2 w-full max-w-sm">
                <Input
                    placeholder="New category"
                    value={newCategory}
                    onChange={(e) => setNewCategory(e.target.value)}
                    className="w-full"
                />
                <Button onClick={handleAdd} className="w-full bg-blue-700 hover:bg-blue-800 cursor-pointer">
                    Add Category
                </Button>
            </div>
      </div>


    </div>
  );
}
