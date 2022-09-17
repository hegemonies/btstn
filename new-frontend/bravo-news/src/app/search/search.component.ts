import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  @Output() newsEmitter = new EventEmitter<string>();
  
  readonly inputForm = new UntypedFormGroup({
      tagValue: new UntypedFormControl(""),
  });

  constructor() { }

  ngOnInit(): void { }

  searchNews(tag: string): void {
    this.newsEmitter.emit(tag);
  }
}
