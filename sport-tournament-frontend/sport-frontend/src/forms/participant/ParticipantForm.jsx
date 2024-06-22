import React, { useState, useEffect } from "react";
import axios from "axios";
import "./ParticipantForm.css";
import { Link, useNavigate } from "react-router-dom";

const ParticipantForm = ({ tournamentName, onClose }) => {
  const [teams, setTeams] = useState([]);
  const [selectedTeam, setSelectedTeam] = useState("");
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTeams = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          "http://localhost:8080/teams/my-teams",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setTeams(response.data);
      } catch (err) {
        setError("Error fetching teams. Please try again later.");
      }
    };

    fetchTeams();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const token = localStorage.getItem("token");
      await axios.post(
        "http://localhost:8080/tournament-participant",
        {
          tournamentName: tournamentName,
          teamName: selectedTeam,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      onClose();
    } catch (err) {
      setError("Error signing up for tournament. Please try again later.");
    }
  };

  return (
    <div className="participant-form">
      <h2>Sign Up for {tournamentName}</h2>
      {error && <p>{error}</p>}
      <form onSubmit={handleSubmit}>
        <label htmlFor="team">Select Team:</label>
        <select
          id="team"
          value={selectedTeam}
          onChange={(e) => setSelectedTeam(e.target.value)}
          required
        >
          <option value="">Select your team</option>
          {teams.map((team) => (
            <option key={team.id} value={team.name}>
              {team.name}
            </option>
          ))}
        </select>
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
};

export default ParticipantForm;
