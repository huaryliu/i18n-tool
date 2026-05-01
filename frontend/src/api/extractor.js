import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

export const extractorApi = {
  async extract(request) {
    const response = await api.post('/extract', request)
    return response.data
  },

  async getAvailableExtractors() {
    const response = await api.get('/extractors')
    return response.data
  },

  async healthCheck() {
    const response = await api.get('/health')
    return response.data
  }
}
