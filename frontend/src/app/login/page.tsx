"use client";


import { useRouter } from "next/navigation";
import Image from "next/image";

export default function LoginPage() {
  const router = useRouter();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-400 to-purple-500 px-4">
      <div className="w-full max-w-sm bg-white p-10 rounded-2xl shadow-2xl text-center">
        <div className="flex justify-center mb-6">
          <Image src="/google-g.svg" alt="Google Logo" width={48} height={48} />
        </div>
        <h1 className="text-3xl font-extrabold text-gray-800 mb-8">
          Finance Tracker
        </h1>
        <button
          className="flex items-center justify-center gap-3 w-full py-3 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 hover:shadow-lg transition duration-300"
        >
          {/* <Image src="/google-logo.svg" alt="Google" width={20} height={20} /> */}
          Sign in with Google
        </button>
        <p className="mt-6 text-gray-500 text-sm">
          Securely login with your Google account
        </p>
      </div>
    </div>
  );
}
