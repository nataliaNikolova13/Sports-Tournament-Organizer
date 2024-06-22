import React, { useState, useEffect } from "react";
import axios from "axios";
import "./ResultPage.css";
import { Link } from "react-router-dom";

const ResultPage = ({ decodeToken }) => {
  return (
    <>
      <div className="results-container">
        <h1>My Results</h1>
      </div>
    </>
  );
};

export default ResultPage;
