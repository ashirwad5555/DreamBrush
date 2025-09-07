import React, { useState } from "react";
import "./App.css";
import ImageDisplay from "./components/ImageDisplay";

function App() {
  const [prompt, setPrompt] = useState("");
  const [imageData, setImageData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!prompt.trim()) {
      setError("Please enter a prompt");
      return;
    }

    setLoading(true);
    setError(null);
    setImageData(null);

    try {
      const response = await fetch("http://localhost:8080/api/generate-image", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ prompt }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error ${response.status}`);
      }

      const data = await response.json();

      setImageData(data);
    } catch (err) {
      setError(`Failed to generate image: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-container">
      <h1 className="main-title">
        <span role="img" aria-label="sparkle">
          ✨
        </span>{" "}
        AI Image Generator
      </h1>

      <form onSubmit={handleSubmit} className="prompt-form">
        <input
          type="text"
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          placeholder="Describe your dream image..."
          className="prompt-input"
        />
        <button type="submit" disabled={loading} className="generate-button">
          {loading ? "Generating..." : "Generate"}
        </button>
      </form>

      {error && <div className="error-message">{error}</div>}

      {loading && (
        <div className="loading">
          <div className="loading-spinner"></div>
          Generating your image...
        </div>
      )}

      <ImageDisplay imageData={imageData} />
    </div>
  );
}

export default App;
