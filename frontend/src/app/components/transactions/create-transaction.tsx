'use client'

import { useState, useEffect } from "react"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectTrigger, SelectContent, SelectItem, SelectValue } from "@/components/ui/select"
import { CategoryDTO, createTransaction, getCategories, TransactionDTO } from "@/app/utils/api"

export function CreateTransaction() {
  const [categories, setCategories] = useState<CategoryDTO[]>([])
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(null)
  const [type, setType] = useState<string>("")
  const [status, setStatus] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getCategories()
      .then((data) => {
        setCategories(data)
        setLoading(false)
      })
      .catch((err) => {
        console.error("Failed to load categories:", err)
        setLoading(false)
      })
  }, [])

  // Update type when category changes
  useEffect(() => {
    if (selectedCategoryId !== null) {
      const selectedCat = categories.find(cat => cat.id === selectedCategoryId)
      if (selectedCat) {
        setType(selectedCat.type)
      } else {
        setType("")
      }
    } else {
      setType("")
    }
  }, [selectedCategoryId, categories])

  function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
  e.preventDefault()
  if (!selectedCategoryId) {
    setStatus("Please select a category.")
    return
  }

  const formData = new FormData(e.currentTarget)
  const transaction: TransactionDTO = {
    amount: Number(formData.get("amount")),
    description: (formData.get("description") as string) || "",
    date: formData.get("date") as string,
    categoryId: selectedCategoryId // backend infers type
  }

  createTransaction(transaction)
    .then(() => {
      e.currentTarget.reset()
      setSelectedCategoryId(null)
      setStatus("Transaction saved successfully!")
    })
    .catch((err) => {
      console.error("Failed to create transaction:", err)
      setStatus("Failed to save transaction.")
    })
}


  return (
    <Card className="max-w-md mx-auto">
      <CardHeader>
        <CardTitle>New Transaction</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input type="number" name="amount" placeholder="Amount" min="0" step="0.01" required />
          <Input type="text" name="description" placeholder="Description (optional)" />
          <Input type="date" name="date" defaultValue={new Date().toISOString().split("T")[0]} required />

          {/* Category Select */}
          <Select value={selectedCategoryId ? String(selectedCategoryId) : undefined} onValueChange={setSelectedCategoryId}>
            <SelectTrigger>
              <SelectValue placeholder="Select category" />
            </SelectTrigger>
            <SelectContent>
              {categories.map(cat => (
                <SelectItem key={cat.id} value={String(cat.id)}>
                  {cat.name} ({cat.type})
                </SelectItem>
              ))}
            </SelectContent>
          </Select>


          {/* Show type for user info (optional) */}
          {type && (
            <div className="text-sm text-gray-500">Type: <span className="font-semibold">{type}</span></div>
          )}

          {/* Makes selected value part of the form if you ever need it */}
          <input type="hidden" name="categoryId" value={selectedCategoryId ?? ""} />

          {status && <p className="text-sm">{status}</p>}

          <Button type="submit" className="w-full">Save Transaction</Button>
        </form>
      </CardContent>
    </Card>
  )
}
