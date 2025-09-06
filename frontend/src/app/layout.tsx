import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { AuthProvider } from "@/app/context/AuthContext"; // adjust path if needed

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Personal Finance Tracker",
  description: "Track and manage your finances easily.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased bg-gray-100`}
      >
        <AuthProvider>
          <div className="flex flex-col h-screen">
            {/* Header */}
            <header className="flex h-16 shrink-0 items-center border-b px-4 bg-white shadow-sm">
              <h1 className="text-xl font-semibold mx-auto">
                Personal Finance Tracker
              </h1>
            </header>

            {/* Main Content */}
            <main className="flex-1 overflow-auto p-6 bg-gray-50">
              {children}
            </main>
          </div>
        </AuthProvider>
      </body>
    </html>
  );
}