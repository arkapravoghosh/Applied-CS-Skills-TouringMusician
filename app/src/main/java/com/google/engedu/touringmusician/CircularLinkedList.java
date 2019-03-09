/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;
import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        Node(Point p) { this.point = p; }
    }

    private Node head = null;

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        Point prevPoint;
        Node curr = head;
        if(head.next != head) {
            prevPoint = curr.point;
            do {
                curr = curr.next;
                total += distanceBetween(prevPoint, curr.point);
                prevPoint = curr.point;
            }while (curr != head);
        }
        return total;
    }

    private void firstNode(Node node) {
        head = node;
        head.prev = head;
        head.next = head;
    }

    private void insertNode(Node node, Node curr) {
        node.next = curr;
        node.prev = curr.prev;
        curr.prev = node;
        node.prev.next = node;
    }

    public void insertBeginning(Point p) {
        Node node = new Node(p);
        if(head != null) {
            insertNode(node, head);
            head = node;
        } else
            firstNode(node);
    }

    public void insertNearest(Point p) {
        Node node = new Node(p), best = null, curr = head;
        double bestDis = Double.POSITIVE_INFINITY;
        if (head == null) {
            firstNode(node);
        } else {
            do {
                curr = curr.next;
                if (distanceBetween(curr.point, p) < bestDis) {
                    bestDis = distanceBetween(curr.point, p);
                    best = curr;
                }
            } while (curr != head);
            if(distanceBetween(p, best.prev.point) < distanceBetween(p, best.next.point)) {
                insertNode(node, best);
            } else {
                insertNode(node, best.next);
            }
        }
    }

    public void insertSmallest(Point p) {
        Node node = new Node(p), best = null, curr = head;
        double distance = Double.POSITIVE_INFINITY;
        if(head == null) {
            firstNode(node);
        } else if(head.next == head) {
            insertNode(node, head);
        } else{
            do {
                double dist = totalDistance() - distanceBetween(curr.point, curr.next.point);
                dist += distanceBetween(p, curr.point) + distanceBetween(p, curr.next.point);
                if(dist < distance) {
                    distance = dist;
                    best = curr.next;
                }
                curr = curr.next;
            }while (curr != head);
            insertNode(node, best);
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
