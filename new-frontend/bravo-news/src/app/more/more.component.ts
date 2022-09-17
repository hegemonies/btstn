import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-more',
  templateUrl: './more.component.html',
  styleUrls: ['./more.component.scss']
})
export class MoreComponent implements OnInit {
  @Output() eventEmitter = new EventEmitter<string>();

  constructor() { }

  ngOnInit(): void {
  }

  moreNews(): void {
    this.eventEmitter.emit("");
  }
}
