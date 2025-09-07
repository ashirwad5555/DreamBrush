import React from "react";
import "./ImageDisplay.css";

const ImageDisplay = ({ imageData }) => {
  if (!imageData) return null;

  const hasImage =
    imageData.artifacts &&
    imageData.artifacts.length > 0 &&
    imageData.artifacts[0].base64;

  const imageSrc = hasImage
    ? `data:image/png;base64,${imageData.artifacts[0].base64}`
    : null;

  const handleCopy = async () => {
    if (!imageSrc) return;
    try {
      const res = await fetch(imageSrc);
      const blob = await res.blob();
      await navigator.clipboard.write([
        new window.ClipboardItem({ [blob.type]: blob }),
      ]);
      alert("Image copied to clipboard!");
    } catch (err) {
      alert("Copy failed: " + err.message);
    }
  };

  const handleDownload = () => {
    if (!imageSrc) return;
    const link = document.createElement("a");
    link.href = imageSrc;
    link.download = "generated-image.png";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="image-container">
      <h2>Generated Image</h2>
      {hasImage ? (
        <>
          <img src={imageSrc} alt="Generated" className="generated-image" />
          <div className="image-actions">
            <button onClick={handleCopy} className="image-action-btn">
              Copy
            </button>
            <button onClick={handleDownload} className="image-action-btn">
              Download
            </button>
          </div>
        </>
      ) : (
        <div className="api-response">
          <h3>API Response:</h3>
          <pre>{JSON.stringify(imageData, null, 2)}</pre>
        </div>
      )}
    </div>
  );
};

export default ImageDisplay;
