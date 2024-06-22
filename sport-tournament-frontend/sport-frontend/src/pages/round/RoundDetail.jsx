import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import "./RoundDetail.css";

const RoundDetail = () => {
  const { id } = useParams();
  const [round, setRound] = useState(null);
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRoundDetails = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/round/${id}/matches`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setRound(response.data);
        setMatches(response.data);
        console.log(response.data);
      } catch (err) {
        setError("Error fetching round details. Please try again later.");
      }
    };
    fetchRoundDetails();
  }, [id]);

  if (error) {
    return <p>{error}</p>;
  }
  if (!round) {
    return <p>Loading...</p>;
  }

  return (
    <div>
      <h1>Round {round.roundNumber}</h1>
      <h2>Matches</h2>
      <div className="matches-container">
        {matches.map((match) => (
          <div key={match.matchId} className="match-card">
            <p>Team 1: {match.team1Name}</p>
            <p>Team 2: {match.team2Name}</p>
            <p>Score Team 1: {match.scoreTeam1}</p>
            <p>Score Team 2: {match.scoreTeam2}</p>
            <p>Winning Team: {match.winningTeamName}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default RoundDetail;
