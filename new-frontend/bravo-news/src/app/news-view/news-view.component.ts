import { Component, Input, OnInit } from '@angular/core';
import {  NewsItemView } from '../dto/interfaces';

@Component({
  selector: 'app-news-view',
  templateUrl: './news-view.component.html',
  styleUrls: ['./news-view.component.scss']
})
export class NewsViewComponent implements OnInit {
  @Input() news: NewsItemView[] = [];

  constructor() { }

  ngOnInit(): void { }

}
