import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import "./TeamDetail.css";
import ParticipantForm from "../../forms/participant/ParticipantForm";

const TeamDetail = ({ decodeToken }) => {
  const { teamId } = useParams();
  const [team, setTeam] = useState("");
  const [participants, setParticipants] = useState([]);
  const [error, setError] = useState(null);
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [added, setAdded] = useState(false);

  useEffect(() => {
    const fetchTeam = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/teams/${teamId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setTeam(response.data);
      } catch (error) {
        setError(error.response?.body||"There was an error fetching the team.");
      }
    };

    fetchTeam();
  }, [teamId]);

  useEffect(() => {
    const fetchTeamParticipants = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/participant/team/${teamId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setParticipants(response.data);
      } catch (error) {
        setError(error.response?.body||"There was an error fetching the team Participants.");
      }
    };

    fetchTeamParticipants();
  }, [teamId, added]);

    const closeParticipantForm = () => {
      setAdded(!added);
    };

  return (
      <div className="team-detail-container">
        <h2>{team.name}</h2>
        <p>Category: {team.category}</p>
        <h2>Team Participants</h2>
        <ul>
          {participants.map((participant) => (
            <li key={participant.id}>
              {participant.user.email} - Status: {participant.status}
            </li>
          ))}
        </ul>
        <div className="participant-section">
          <ParticipantForm teamId={teamId} onClose={closeParticipantForm} />
        </div>
        {error && <div className="error-message">{error}</div>}
      </div>
    );
  };

export default TeamDetail;
