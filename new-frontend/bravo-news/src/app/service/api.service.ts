import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { NewsResponse } from "../dto/interfaces";

@Injectable()
export class ApiService {

  constructor(private http: HttpClient) {}

  getNews(tag: string, limit: number, offset: number): Observable<NewsResponse> {
    return this.http.post<NewsResponse>(
        environment.apiUrl + "api/news", 
        {
          tag: tag,
          pagination: {
            limit: limit,
            offset: offset
          }
        }
    );
  }
}
