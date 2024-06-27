import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import "./TeamDetail.css";

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
      } catch (err) {
        setError("There was an error fetching the team.");
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
      } catch (err) {
        setError("There was an error fetching the team Participants.");
      }
    };

    fetchTeamParticipants();
  }, [teamId, added]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/user", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setUsers(response.data);
      } catch (err) {
        setError("There was an error fetching users.");
      }
    };

    fetchUsers();
  }, []);

  const handleAddParticipant = async () => {
    setAdded(false);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/participant",
        {
          userId: selectedUser,
          teamId: teamId,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("Participant added:", response.data);
      setAdded(true);
    } catch (err) {
      setError("There was an error adding the participant.");
    }
  };

  const handleDeleteParticipant = async () => {
    setAdded(true);
    try {
      const token = localStorage.getItem("token");
      console.log(selectedUser);
      await axios.delete("http://localhost:8080/participant", {
        data: {
          userId: selectedUser,
          teamId: teamId,
        },
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      setAdded(false);
      //   selectedUser(null);
    } catch (err) {
      setError("There was an error deleting the participant.");
    }
  };

  if (error) return <div>{error}</div>;

  return (
    <div className="team-detail-container">
      <h2>{team.name}</h2>
      <p>Category: {team.category}</p>
      <h2>Team Participants</h2>
      <ul>
        {participants.map((participant) => (
          <li key={participant.id}>
            {participant.user.fullName} - Status: {participant.status}
          </li>
        ))}
      </ul>
      <div className="participant-section">
        <h3>Add Participant</h3>
        <select
          value={selectedUser}
          onChange={(e) => setSelectedUser(e.target.value)}
        >
          <option value="">Select a user</option>
          {users.map((user) => (
            <option key={user.id} value={user.id}>
              {user.fullName}
            </option>
          ))}
        </select>
        <button onClick={handleAddParticipant}>Add Participant</button>
        <h3>Delete Participant</h3>
        <select
          value={selectedUser}
          onChange={(e) => setSelectedUser(e.target.value)}
        >
          <option value="">Select a user</option>
          {users.map((user) => (
            <option key={user.id} value={user.id}>
              {user.fullName}
            </option>
          ))}
        </select>
        <button onClick={handleDeleteParticipant}>Delete Participant</button>
      </div>
      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default TeamDetail;