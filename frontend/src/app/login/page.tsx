"use client";

import { auth, googleProvider } from "../firebase";
import { signInWithPopup } from "firebase/auth";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const router = useRouter();

  const handleGoogleLogin = async () => {
    try {
      await signInWithPopup(auth, googleProvider);
      router.push("/dashboard");
    } catch (err: any) {
      alert(err.message || "Failed to login");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-400 to-purple-500 px-4">
      <div className="w-full max-w-sm bg-white p-10 rounded-2xl shadow-2xl text-center">
        {/* Google Logo */}
        <div className="flex justify-center mb-6">
          <img
            src="/google-g.svg" // download the official Google "G" logo and place it in /public
            alt="Google Logo"
            className="w-12 h-12"
          />
        </div>

        <h1 className="text-3xl font-extrabold text-gray-800 mb-8">
          Finance Tracker
        </h1>

        <button
          onClick={handleGoogleLogin}
          className="flex items-center justify-center gap-3 w-full py-3 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 hover:shadow-lg transition duration-300"
        >
          {/* <img src="/google-logo.svg" alt="Google" className="w-5 h-5" /> */}
          Sign in with Google
        </button>

        <p className="mt-6 text-gray-500 text-sm">
          Securely login with your Google account
        </p>
      </div>
    </div>
  );
}
