import {Route, Routes} from 'react-router-dom'
import Register from "./pages/Register";
import 'bootstrap/dist/css/bootstrap.min.css';
import Container from 'react-bootstrap/Container'
import Navbar from "./components/Navbar";

function App() {
  return (
    <>
      <Navbar/>
      <Container>
        <Routes>
          <Route path="register" element={<Register/>}/>
        </Routes>
      </Container>
    </>

  );
}

export default App;
