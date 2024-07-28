export function isTokenExpired(token: string): boolean {
    if (!token) return true;

    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const currentTime = Date.now() / 1000;
  
    return decodedToken.exp < currentTime;
  }