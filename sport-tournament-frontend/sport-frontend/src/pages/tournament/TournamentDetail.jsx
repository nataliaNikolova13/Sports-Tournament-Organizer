import React, { useState, useEffect } from "react";
import axios from "axios";
import "./TournamentDetail.css";
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import Modal from "../../modal/Modal";
import ParticipantForm from "../../forms/participant/ParticipantForm";
import { jwtDecode } from "jwt-decode";

const TournamentDetail = () => {
  const { id } = useParams();
  const [tournament, setTournament] = useState(null);
  const [error, setError] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [rounds, setRounds] = useState([]);
  const [participants, setParticipants] = useState([]);
  const [isAdmin, setIsAdmin] = useState(false);
  const [participantToDelete, setParticipantToDelete] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decoded = jwtDecode(token);
      if (decoded.role === "[ROLE_Admin]") {
        setIsAdmin(true);
      }
    }
  }, []);

  useEffect(() => {
    const fetchTournament = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/tournament/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setTournament(response.data);
      } catch (err) {
        setError(
          err.response?.data ||
            "Error fetching tournament details. Please try again later."
        );
      }
    };

    fetchTournament();
  }, [id]);

  useEffect(() => {
    const fetchRounds = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/round/tournament/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setRounds(response.data);
      } catch (err) {
        setError(
          err.response?.data || "Error fetching rounds. Please try again later."
        );
      }
    };

    fetchRounds();
  }, [id]);

  useEffect(() => {
    const fetchParticipants = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/tournament-participant/participants/teams/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setParticipants(response.data);
        console.log(response.data);
      } catch (err) {
        setError(
          err.response?.data ||
            "Error fetching participants. Please try again later."
        );
      }
    };

    fetchParticipants();
  }, [id, rounds]);

  const handleDelete = async () => {
    console.log(1);
    if (!participantToDelete) return;
    console.log(2);

    try {
      const token = localStorage.getItem("token");
      await axios.delete(`http://localhost:8080/tournament-participant`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        data: {
          tournamentName: tournament.tournamentName,
          teamName: participantToDelete.team.name,
        },
      });

      setParticipants(
        participants.filter(
          (participant) => participant !== participantToDelete
        )
      );
      setParticipantToDelete(null);
    } catch (err) {
      setError(
        err.response?.data ||
          "Error deleting participant. Please try again later."
      );
    }
  };

  useEffect(() => {
    handleDelete();
  }, [participantToDelete]);

  if (error) {
    return <p>{error}</p>;
  }
  if (!tournament) {
    return <p>Loading...</p>;
  }

  const handleDeleteTournament = async () => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`http://localhost:8080/tournament/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      navigate("/tournaments");
    } catch (err) {
      setError(
        err.response?.data ||
          "Error deleting tournament. Please try again later."
      );
    }
  };

  return (
    <>
      <div>
        <h1>{tournament.tournamentName}</h1>
        <p>
          <strong>Sport Type:</strong> {tournament.sportType}
        </p>

        <p>
          <strong>Category:</strong> {tournament.tournamentCategory}
        </p>

        <p>
          <strong>Location:</strong> {tournament.locationName}
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
          <strong>Match Duration:</strong> {tournament.matchDuration} minutes
        </p>
        <p>
          <strong>Team Members Count:</strong> {tournament.teamMemberCount}
        </p>
        {isAdmin && (
          <button className="delete-btn" onClick={handleDeleteTournament}>
            Delete Tournament
          </button>
        )}
        <button onClick={() => setIsModalOpen(true)}>
          Sign Up for Tournament
        </button>

        <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
          <ParticipantForm
            tournamentName={tournament.tournamentName}
            onClose={() => setIsModalOpen(false)}
          />
        </Modal>
      </div>

      <div className="round-div">
        {rounds.length > 0 ? (
          <>
            <h1>Past rounds</h1>
            {rounds.map((round) => (
              <div key={round.id}>
                <Link to={`/round/${round.id}`}>Round {round.roundNumber}</Link>
              </div>
            ))}
          </>
        ) : (
          <div className="participants-div">
            <h1>Participants</h1>
            {participants.length > 0 ? (
              participants.map((participant) => (
                <div key={participant.id}>
                  <p>
                    <strong>Name:</strong> {participant.team.name} -{" "}
                    <strong>Date:</strong>{" "}
                    {new Date(participant.timeStamp).toLocaleDateString()} -
                    <strong> Status:</strong> {participant.status}
                    {isAdmin && participant.status === "joined" && (
                      <button
                        onClick={() => {
                          setParticipantToDelete(participant);
                        }}
                      >
                        Delete
                      </button>
                    )}
                  </p>
                </div>
              ))
            ) : (
              <p>No participants found.</p>
            )}
          </div>
        )}
      </div>
    </>
  );
};

export default TournamentDetail;
