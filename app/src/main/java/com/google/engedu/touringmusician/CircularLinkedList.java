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
        public Node(Point point){
            this.point=point;
            this.prev=null;
            this.next=null;
        }
    }

    Node head;

    public void insertBeginning(Point p) {
        Node newNode=new Node(p);
        if (head==null){
            head=newNode;
            head.prev=head;
            head.next=head;
        }
        else {
            Node oldNode=head.prev;
            oldNode.next=newNode;
            newNode.prev=oldNode;
            newNode.next=head;
            head.prev=newNode;
        }

    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        CircularLinkedListIterator iterator=new CircularLinkedListIterator();
        while (iterator.hasNext()){
            Point p1=iterator.current.point;
            Point p2=iterator.current.next.point;
            total+=distanceBetween(p1,p2);
            iterator.next();
        }
        return total;
    }

    public void insertNearest(Point p) {
        CircularLinkedListIterator iterator=new CircularLinkedListIterator();
        Node newNode=new Node(p);
        if (head==null){
            head=newNode;
            head.prev=head;
            head.next=head;
        }
        else {
            Node nearestNode=null;
            float nearestDist=999999;
            while (iterator.hasNext()){
                if (nearestNode==null){
                    nearestNode=iterator.current;
                    nearestDist=distanceBetween(iterator.current.point,p);
                }
                else {
                    float distance=distanceBetween(iterator.current.point,p);
                    if (nearestDist>distance){
                        nearestDist=distance;
                        nearestNode=iterator.current;
                    }
                }
                iterator.next();
            }
            Node tempPrev=nearestNode.prev;
            nearestNode.prev=newNode;
            newNode.next=nearestNode;
            newNode.prev=tempPrev;
            tempPrev.next=newNode;

        }

    }

    public void insertSmallest(Point p) {
        Node newNode=new Node(p);
        if (head==null){
            head=newNode;
            head.prev=head;
            head.next=head;
        }
        else {
            CircularLinkedListIterator iterator=new CircularLinkedListIterator();
            Node smallestDistParentNode=null;
            float smallestChange =999999;
            while (iterator.hasNext()){
                Node currentNode=iterator.current;
                Node previousNode=currentNode.prev;
                if (previousNode==currentNode){
                    insertBeginning(p);
                }
                else if (smallestDistParentNode==null){
                    smallestDistParentNode=currentNode;
                    float currentDist=distanceBetween(currentNode.point,previousNode.point);
                    float changedDist=distanceBetween(currentNode.point,newNode.point)+distanceBetween(previousNode.point,newNode.point);
                    smallestChange=changedDist-currentDist;
                }
                else {
                    float currentDist=distanceBetween(currentNode.point,previousNode.point);
                    float changedDist=distanceBetween(currentNode.point,newNode.point)+distanceBetween(previousNode.point,newNode.point);
                    float difference=changedDist-currentDist;
                    if(smallestChange>difference){
                        smallestChange=difference;
                        smallestDistParentNode=currentNode;
                    }
                }
                iterator.next();
            }
            Node tempPrev = smallestDistParentNode.prev;

            smallestDistParentNode.prev = newNode;
            newNode.next = smallestDistParentNode;
            newNode.prev = tempPrev;
            tempPrev.next = newNode;
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
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
