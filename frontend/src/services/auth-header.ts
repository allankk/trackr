export default function authHeader(): { Authorization?: string } {
  const storedUser = localStorage.getItem('user');

  // Check if storedUser is a non-empty string before parsing
  let user: { accessToken?: string } | null = null;
  if (storedUser) {
    try {
      user = JSON.parse(storedUser);
    } catch (e) {
      console.error('Error parsing stored user:', e);
    }
  }

  // Return authorization header if user and accessToken are available
  if (user && user.accessToken) {
    return { Authorization: 'Bearer: ' + user.accessToken };
  } else {
    return {};
  }
}
