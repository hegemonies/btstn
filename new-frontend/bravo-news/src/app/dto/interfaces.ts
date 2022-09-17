export interface NewsResponse {
  view: NewsItem[];
  total: number;
}

export interface ErrorResponse {
  cause: string;
  code: string;
  message: string;
}

export interface NewsItem {
  date: number;
  message: string;
  source: string;
}

export interface NewsItemView {
  message: string;
  sourceWithDateTime: string;
}
  
export interface Pagination {
  offset: number;
  limit: number;
}
