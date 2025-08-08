import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import CategoriesPage from './pages/CategoriesPage';
import Dashboard from './pages/Dashboard';
import TransactionsPage from './pages/TransactionsPage';

function App() {
  return (
    <Router>
      <main>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/transactions" element={<TransactionsPage />} />
          <Route path="/categories" element={<CategoriesPage/>} />
        </Routes>
      </main>
    </Router>
  );
}

export default App;
