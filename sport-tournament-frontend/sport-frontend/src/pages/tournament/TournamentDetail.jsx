import React, { useState, useEffect } from "react";
import axios from "axios";
import "./TournamentDetail.css";
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";
import Modal from "../../modal/Modal";
import ParticipantForm from "../../forms/participant/ParticipantForm";

const TournamentDetail = () => {
  const { id } = useParams();
  const [tournament, setTournament] = useState(null);
  const [error, setError] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [rounds, setRounds] = useState([]);

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
        setError("Error fetching tournament details. Please try again later.");
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
        setError("Error fetching rounds. Please try again later.");
      }
    };
    fetchRounds();
  }, [id]);
  if (error) {
    return <p>{error}</p>;
  }
  if (!tournament) {
    return <p>Loading...</p>;
  }

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
        {rounds.length > 0 && <h1>Past rounds</h1>}
        {rounds.map((round) => (
          <div key={round.id}>
            <Link to={`/round/${round.id}`}>Round {round.roundNumber}</Link>
          </div>
        ))}
      </div>
    </>
  );
};

export default TournamentDetail;
