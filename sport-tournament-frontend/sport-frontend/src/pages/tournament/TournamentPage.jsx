import React, { useState, useEffect } from "react";
import axios from "axios";
import "./TournamentPage.css";
import { Link } from "react-router-dom";
import CreateTournamentForm from "../../forms/tournamentCreate/CreateTournamentForm";

const TournamentPage = ({ decodeToken }) => {
  const [tournaments, setTournaments] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/tournament", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setTournaments(response.data);
        console.log(response.data);
      } catch (err) {
        setError(err.response?.data || "Error fetching tournaments. Please try again later.");
      }
    };

    fetchTournaments();
  }, []);
  return (
    <>
      <CreateTournamentForm></CreateTournamentForm>
      <h1>Tournaments</h1>
      <div className="tournament-list">
        {tournaments.map((tournament) => (
          <div key={tournament.id} className="tournament-card">
            <h2>{tournament.tournamentName}</h2>
            <p>
              <strong>Sport Type:</strong> {tournament.sportType}
            </p>
            <p>
              <strong>Category:</strong> {tournament.tournamentCategory}
            </p>
            <Link to={`/tournament/${tournament.id}`}>View Details</Link>
          </div>
        ))}
      </div>
    </>
  );
};

export default TournamentPage;
