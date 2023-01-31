import { Alert } from "react-bootstrap";

export default function ErrorAlert(props) {
  return (
    <Alert variant="danger">
      {props.message}
    </Alert>
  )
}