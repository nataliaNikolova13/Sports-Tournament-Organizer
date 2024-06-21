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
        setError("Error fetching tournaments. Please try again later.");
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
            {/* <p>
              <strong>Location:</strong> {tournament.location.name}
            </p>
            <p>
              <strong>Start Date:</strong>{" "}
              {new Date(tournament.startAt).toLocaleDateString()}
            </p>
            <p>
              <strong>End Date:</strong>{" "}
              {new Date(tournament.endAt).toLocaleDateString()}
            </p>
            <p>
              <strong>Start Hour:</strong> {tournament.startHour}
            </p>
            <p>
              <strong>End Hour:</strong> {tournament.endHour}
            </p>
            <p>
              <strong>Team Count:</strong> {tournament.teamCount}
            </p>
            <p>
              <strong>Match Duration:</strong> {tournament.matchDuration}{" "}
              minutes
            </p>
            <p>
              <strong>Team Members Count:</strong> {tournament.teamMemberCount}
            </p> */}
            <Link to={`/tournament/${tournament.id}`}>View Details</Link>
          </div>
        ))}
      </div>
    </>
  );
};

export default TournamentPage;
