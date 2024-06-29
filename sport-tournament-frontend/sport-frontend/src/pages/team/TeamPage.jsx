import React, { useState, useEffect } from "react";
import axios from "axios";
import "./TeamPage.css";
import CreateTeam from "../../forms/teamCreate/CreateTeam";
import { Link } from "react-router-dom";

const TeamPage = ({ decodeToken }) => {
  const [teams, setTeams] = useState([]);
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
        setError(
          err.response?.data || "There was an error fetching the teams."
        );
      }
    };

    fetchTeams();
  }, []);
  return (
    <>
      <CreateTeam></CreateTeam>
      <h2>My Teams</h2>
      <ul className="ul-teams">
        {teams.map((team) => (
          <li className="li-element" key={team.id}>
            <Link to={`/teams/${team.id}`} className="team-link">
              <h3>{team.name}</h3>
              <p>Category: {team.category}</p>
            </Link>
          </li>
        ))}
      </ul>
    </>
  );
};

export default TeamPage;
