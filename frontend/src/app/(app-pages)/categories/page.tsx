import CategoryList from "../../components/categories/category-list";
import { CategoriesCard } from "../../components/dashboard/categories-card";

export default function categories() {
  return (
    <main>
      <CategoriesCard />
      <CategoryList />
    </main>
  )
}