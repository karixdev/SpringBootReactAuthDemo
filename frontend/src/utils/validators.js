export const emailValidator = email => {
  const regex = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/;
  return regex.test(email);
}

export const passwordValidator = password => {
  return password.length >= 8 && password.length <= 32;
}