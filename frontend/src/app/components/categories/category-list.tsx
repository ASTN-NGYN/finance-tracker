'use client'

import { useState, useEffect } from "react";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Trash2 } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from "@/components/ui/dialog";

import { getCategories, createCategory, deleteCategory, CategoryDTO, CreateCategoryDTO } from "@/app/utils/api";

interface Category {
    id: number;
    name: string;
    type: "INCOME" | "SAVING" | "EXPENSE";
    description?: string;
}

export default function CategoryList() {
    const [categories, setCategories] = useState<Category[]>([]);
    const [newName, setNewName] = useState("");
    const [newType, setNewType] = useState<"" | "INCOME" | "SAVING" | "EXPENSE">("");

    const [newDescription, setNewDescription] = useState("");
    const [deleteId, setDeleteId] = useState<number | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);

    // Fetch categories from API
    const fetchCategories = async () => {
        try {
            const data: CategoryDTO[] = await getCategories();
            if (Array.isArray(data)) {
                const mapped = data.map((cat) => ({
                    id: cat.id,
                    name: cat.name,
                    description: cat.description,
                    type: cat.type as "INCOME" | "SAVING" | "EXPENSE",
                }));
                setCategories(mapped);
            }
        } catch (err) {
            console.error("Failed to fetch categories:", err);
            alert("Failed to fetch categories.");
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    // Add category
    const handleAdd = async () => {
        // Validate required fields
        if (!newName.trim()) {
            alert("Category name is required.");
            return;
        }
        if (!newType) {
            alert("Category type is required.");
            return;
        }

        const categoryToCreate: CreateCategoryDTO = {
            name: newName.trim(),
            type: newType,
            description: newDescription.trim(),
        };

        try {
            await createCategory(categoryToCreate);
            window.location.reload(); // full page refresh
        } catch (err) {
            alert("Failed to create category.");
        }
    };

    // Delete category
    const confirmDelete = async () => {
        if (deleteId === null) return;

        try {
            await deleteCategory(deleteId);
            window.location.reload(); // full page refresh
        } catch (err) {
            alert("Failed to delete category.");
        }
    };

    return (
        <div className="mt-10 w-full max-w-3xl mx-auto border rounded-md p-4">
            {/* Scrollable category list */}
            <ScrollArea className="h-[400px] mb-4">
                <div className="grid grid-cols-2 gap-4">
                    {categories.map((cat) => (
                        <Card
                            key={cat.id}
                            className="flex flex-col items-start justify-between p-4 hover:shadow-lg transition-shadow rounded-md"
                        >
                            <div>
                                <span className="font-medium text-2xl">{cat.name}</span>
                                <div className="text-sm text-gray-500 mt-1">{cat.type}</div>
                            </div>
                            <Button
                                variant="ghost"
                                size="icon"
                                onClick={() => {
                                    setDeleteId(cat.id);
                                    setDialogOpen(true);
                                }}
                                className="self-end"
                            >
                                <Trash2 className="w-5 h-5 text-red-500" />
                            </Button>
                        </Card>
                    ))}
                </div>
            </ScrollArea>

            {/* Add new category form */}
            <div className="flex flex-col gap-2 w-full">
                <Input
                    placeholder="Category Name"
                    value={newName}
                    onChange={(e) => setNewName(e.target.value)}
                    className="w-full"
                />
                <Input
                    placeholder="Description (optional)"
                    value={newDescription}
                    onChange={(e) => setNewDescription(e.target.value)}
                    className="w-full"
                />
                <select
                    value={newType}
                    onChange={(e) => setNewType(e.target.value as "INCOME" | "SAVING" | "EXPENSE")}
                    className="w-full border rounded-md p-2"
                >
                    <option value="" disabled>
                        Select type
                    </option>
                    <option value="INCOME">INCOME</option>
                    <option value="SAVING">SAVING</option>
                    <option value="EXPENSE">EXPENSE</option>
                </select>

                <Button
                    onClick={handleAdd}
                    className="w-full bg-blue-700 hover:bg-blue-800 cursor-pointer"
                    disabled={!newName.trim() || !newType}
                >
                    Add Category
                </Button>
            </div>

            {/* Delete confirmation dialog */}
            <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
                <DialogContent className="sm:max-w-[400px]">
                    <DialogHeader>
                        <DialogTitle>Delete Category</DialogTitle>
                    </DialogHeader>
                    <p className="py-2">
                        Are you sure you want to delete this category? This action is
                        irreversible, and all transactions associated with this category
                        will also be permanently removed.
                    </p>
                    <DialogFooter className="flex justify-end gap-2">
                        <Button variant="outline" onClick={() => setDialogOpen(false)}>
                            Cancel
                        </Button>
                        <Button variant="destructive" onClick={confirmDelete}>
                            Delete
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}
