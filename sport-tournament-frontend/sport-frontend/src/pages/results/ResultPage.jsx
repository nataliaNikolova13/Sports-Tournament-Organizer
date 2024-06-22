import React, { useState, useEffect } from "react";
import axios from "axios";
import "./ResultPage.css";
import { Link } from "react-router-dom";

const ResultPage = ({ decodeToken }) => {
  const [matchResults, setMatchResults] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    axios
      .get("http://localhost:8080/match-result/results", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        setMatchResults(response.data);
      })
      .catch((error) => {
        console.error("Error fetching match results:", error);
      });
  }, []);

  return (
    <div>
      <h1 className="match-result-h1">Match Results</h1>
      {matchResults.map((result) => (
        <div key={result.id} className="match-result">
          <h2 className="match-result-h2">Match Result</h2>
          <p className="match-result-p">
            Round Number: {result.match.round.roundNumber}
          </p>
          <p className="match-result-p">
            Tournament Name: {result.match.round.tournament.tournamentName}
          </p>
          <p className="match-result-p">Team 1: {result.match.team1.name}</p>
          <p className="match-result-p">Team 2: {result.match.team2.name}</p>
          <p className="match-result-p">
            Score: {result.scoreTeam1} - {result.scoreTeam2}
          </p>
          <p className="match-result-p">
            Winning Team: {result.winningTeam.name}
          </p>
        </div>
      ))}
    </div>
  );
};

export default ResultPage;
