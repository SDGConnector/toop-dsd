class BackendClient {
  async get(path) {
    let result;
    try {
      result = await $.ajax({
        method: 'GET',
        url: path
      })
    } catch (error) {
      console.error("Backend client failed: " + error.status + ":" + error.statusText)
      result = ['Cannot', 'Define']
    }
    return result;
  }
}