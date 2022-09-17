import { Component } from '@angular/core';

import { ErrorResponse, NewsItem, NewsItemView, NewsResponse } from './dto/interfaces';
import { ApiService } from './service/api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'bravo-news';
  news: NewsItemView[] = [];
  tag: string | null = null;
  limit: number = 10;
  offset: number = 0;
  total: number = 0;

  constructor(private apiService: ApiService) {}

  renderDate(date: number): string {

    let getDayOfWeekString = (dayOfWeek: number) => {
        if (dayOfWeek === 0) {
            return 'Sun';
        } else if (dayOfWeek === 1) {
            return 'Mon';
        } else if (dayOfWeek === 2) {
            return 'Tue';
        } else if (dayOfWeek === 3) {
            return 'Wed';
        } else if (dayOfWeek === 4) {
            return 'Thu'
        } else if (dayOfWeek === 5) {
            return 'Fri';
        } else if (dayOfWeek === 6) {
            return 'Sat';
        } else {
          throw "Error: failure convert day of week to string";
        }
    }

    let getHours = (hours: number) => {
        if (hours < 10) {
            return '0' + hours;
        } else {
            return hours;
        }
    }

    let getMinutes = (minutes: number) => {
        if (minutes < 10) {
            return '0' + minutes;
        } else {
            return minutes;
        }
    }  

    let d = new Date(date);

    return d.getDate() + '-'
        + d.getMonth() + '-'
        + d.getFullYear() + ' '
        + getHours(d.getHours()) + ':'
        + getMinutes(d.getMinutes()) + ' '
        + getDayOfWeekString(d.getDay());
  }

  convertNewsItemToView(news: NewsItem[]): NewsItemView[] {
    return news.map(newsItem => ({
      message: newsItem.message,
      sourceWithDateTime: this.renderDate(newsItem.date) + " from " + newsItem.source
    }));
  }

  setNews(tag: string) {
    if (tag) {
      this.tag = tag;
      this.offset = 0;
      this.apiService.getNews(this.tag, this.limit, this.offset)
        .subscribe(
          (response: NewsResponse) => {
            this.news = this.convertNewsItemToView(response.view);
            this.total = response.total;
          },
          (error: ErrorResponse) => {
            window.alert(error.message)
            this.news = [];
            this.tag = null;
            this.offset = 0;
          }
        );
    } else {
      console.error("Error: can not search news by empty stock tag!")
    }
  }

  moreNews() {
    if (this.tag) {
      this.offset += 10;
      this.apiService.getNews(this.tag, this.limit, this.offset)
        .subscribe(
          (response: NewsResponse) => {
            this.news.push(...this.convertNewsItemToView(response.view));
            this.total = response.total;
          },
          (error: ErrorResponse) => {
            window.alert(error.message)
          }
        );
    }
  }
}
