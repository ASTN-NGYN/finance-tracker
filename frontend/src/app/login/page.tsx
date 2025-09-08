"use client";

import { auth, googleProvider } from "@/lib/firebase";
import {
  signInWithPopup,
  setPersistence,
  browserLocalPersistence,
  browserSessionPersistence,
} from "firebase/auth";
import { useRouter } from "next/navigation";
import Image from "next/image";
import { PiggyBank, DollarSign, Coins } from "lucide-react";
import { useState } from "react";

export default function LoginPage() {
  const router = useRouter();
  const [stayLoggedIn, setStayLoggedIn] = useState(true);

  const handleGoogleLogin = async () => {
    try {
      await setPersistence(
        auth,
        stayLoggedIn ? browserLocalPersistence : browserSessionPersistence
      );

      await signInWithPopup(auth, googleProvider);
      router.push("/dashboard");
    } catch (err: unknown) {
      if (err instanceof Error) alert(err.message);
      else alert("Failed to login");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-400 to-blue-500 px-4">
      <div className="w-full max-w-sm bg-white p-10 rounded-3xl shadow-2xl text-center relative overflow-hidden">
        <DollarSign className="absolute -top-4 -left-4 text-green-200 w-10 h-10 rotate-12" />
        <PiggyBank className="absolute -bottom-6 -right-6 text-blue-200 w-16 h-16 rotate-12" />
        <Coins className="absolute top-1/3 -right-4 text-yellow-300 w-8 h-8" />

        <h1 className="text-3xl font-extrabold text-gray-800 mb-6">
          Finance Tracker
        </h1>
        <p className="mb-8 text-gray-500 text-sm">
          Track your income, expenses, and savings securely.
        </p>

        <button
          onClick={handleGoogleLogin}
          className="flex items-center justify-center gap-3 w-full py-3 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 hover:shadow-lg transition duration-300"
        >
          <Image src="/google-g.svg" alt="Google" width={20} height={20} />
          Sign in with Google
        </button>

        <label className="mt-4 flex items-center gap-2 text-sm text-gray-600">
          <input
            type="checkbox"
            checked={stayLoggedIn}
            onChange={(e) => setStayLoggedIn(e.target.checked)}
          />
          Stay logged in
        </label>

        <p className="mt-6 text-gray-400 text-sm">
          Sign in securely with your Google account
        </p>
      </div>
    </div>
  );
}
