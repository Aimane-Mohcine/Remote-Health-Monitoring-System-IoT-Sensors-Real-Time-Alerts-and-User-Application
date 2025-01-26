import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WowService {
  private boxes: HTMLElement[] = [];
  private element!: HTMLElement;
  private interval: any;
  private scrolled = true;

  private defaults = {
    boxClass: 'wow',
    animateClass: 'animated',
    offset: 0,
    mobile: true
  };

  private config: any;

  constructor() {
    this.config = { ...this.defaults };
  }

  init(options?: any): void {
    if (options) {
      this.config = { ...this.defaults, ...options };
    }
    this.element = document.documentElement;

    if (document.readyState === 'interactive' || document.readyState === 'complete') {
      this.start();
    } else {
      document.addEventListener('DOMContentLoaded', this.start.bind(this));
    }
  }

  private start(): void {
    this.boxes = Array.from(this.element.getElementsByClassName(this.config.boxClass) as HTMLCollectionOf<HTMLElement>);
    if (this.boxes.length) {
      if (this.disabled()) {
        this.resetStyle();
      } else {
        this.boxes.forEach(box => this.applyStyle(box, true));
        window.addEventListener('scroll', this.scrollHandler.bind(this), false);
        window.addEventListener('resize', this.scrollHandler.bind(this), false);
        this.interval = setInterval(this.scrollCallback.bind(this), 50);
      }
    }
  }

  private stop(): void {
    window.removeEventListener('scroll', this.scrollHandler.bind(this), false);
    window.removeEventListener('resize', this.scrollHandler.bind(this), false);
    if (this.interval) {
      clearInterval(this.interval);
    }
  }

  private applyStyle(box: HTMLElement, hidden: boolean): void {
    const duration = box.getAttribute('data-wow-duration');
    const delay = box.getAttribute('data-wow-delay');
    const iteration = box.getAttribute('data-wow-iteration');
    box.setAttribute('style', this.customStyle(hidden, duration, delay, iteration));
  }

  private resetStyle(): void {
    this.boxes.forEach(box => box.setAttribute('style', 'visibility: visible;'));
  }

  private customStyle(hidden: boolean, duration: string | null, delay: string | null, iteration: string | null): string {
    let style = hidden ? 'visibility: hidden; animation-name: none;' : 'visibility: visible;';
    if (duration) {
      style += `animation-duration: ${duration};`;
    }
    if (delay) {
      style += `animation-delay: ${delay};`;
    }
    if (iteration) {
      style += `animation-iteration-count: ${iteration};`;
    }
    return style;
  }

  private scrollHandler(): void {
    this.scrolled = true;
  }

  private scrollCallback(): void {
    if (this.scrolled) {
      this.scrolled = false;
      this.boxes = this.boxes.filter(box => {
        if (this.isVisible(box)) {
          this.show(box);
          return false;
        }
        return true;
      });
      if (!this.boxes.length) {
        this.stop();
      }
    }
  }

  private show(box: HTMLElement): void {
    this.applyStyle(box, false);
    box.className += ` ${this.config.animateClass}`;
  }

  private offsetTop(element: HTMLElement): number {
    let top = element.offsetTop;
    while (element.offsetParent) {
      element = element.offsetParent as HTMLElement;
      top += element.offsetTop;
    }
    return top;
  }

  private isVisible(box: HTMLElement): boolean {
    const offset = parseInt(box.getAttribute('data-wow-offset') || this.config.offset, 10);
    const viewTop = window.pageYOffset;
    const viewBottom = viewTop + this.element.clientHeight - offset;
    const top = this.offsetTop(box);
    const bottom = top + box.clientHeight;
    return top <= viewBottom && bottom >= viewTop;
  }

  private disabled(): boolean {
    return !this.config.mobile && /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
  }
}
