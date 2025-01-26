import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopWidgeComponent } from './top-widge.component';

describe('TopWidgeComponent', () => {
  let component: TopWidgeComponent;
  let fixture: ComponentFixture<TopWidgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TopWidgeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TopWidgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
