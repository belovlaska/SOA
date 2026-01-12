import React from 'react';
import './ErrorDisplay.css';

const ErrorDisplay = ({ error }) => {
  if (!error) return null;

  return (
    <div className="error-container">
      <div className="error-box">
        <strong>Error {error.code || 500}:</strong>
        <p>{error.message || 'Unknown error occurred'}</p>
      </div>
    </div>
  );
};

export default ErrorDisplay;
