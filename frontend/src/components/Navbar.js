import {Container} from "react-bootstrap";
import {Nav} from "react-bootstrap";
import {Navbar as BNavbar} from "react-bootstrap";
import {Link} from "react-router-dom";

export default function Navbar() {
  return (
    <BNavbar bg="dark" variant="dark">
      <Container>
        <BNavbar.Brand href="/">AuthDemo</BNavbar.Brand>
        <Nav className="me-auto">
          <Link to="/register" className="nav-link">Register</Link>
        </Nav>
      </Container>
    </BNavbar>
  );
}