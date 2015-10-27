/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.currysrc.api.transform.ast;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;

import java.util.List;

/**
 * Locates the {@link org.eclipse.jdt.core.dom.BodyDeclaration} associated with an enum
 * declaration.
 */
public final class EnumConstantLocater implements BodyDeclarationLocater {

  private final TypeLocater enumTypeLocater;

  private final String constantName;

  public EnumConstantLocater(String packageName, String typeName, String constantName) {
    this(new TypeLocater(packageName, typeName), constantName);
  }

  public EnumConstantLocater(TypeLocater enumTypeLocater, String constantName) {
    this.enumTypeLocater = enumTypeLocater;
    this.constantName = constantName;
  }

  @Override
  public boolean matches(BodyDeclaration node) {
    if (!(node instanceof EnumConstantDeclaration)) {
      return false;
    }

    if (enumTypeLocater.matches((BodyDeclaration) node.getParent())) {
      EnumConstantDeclaration enumConstantDeclaration = (EnumConstantDeclaration) node;
      if (enumConstantDeclaration.getName().getFullyQualifiedName().equals(constantName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public BodyDeclaration find(CompilationUnit cu) {

    AbstractTypeDeclaration typeDeclaration = enumTypeLocater.find(cu);
    if (typeDeclaration == null || !(typeDeclaration instanceof EnumDeclaration)) {
      return null;
    }
    for (EnumConstantDeclaration enumConstantDeclaration
        : (List<EnumConstantDeclaration>) ((EnumDeclaration) typeDeclaration).enumConstants()) {
      if (enumConstantDeclaration.getName().getFullyQualifiedName().equals(constantName)) {
        return enumConstantDeclaration;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "EnumConstantLocater{" +
        "enumTypeLocater=" + enumTypeLocater +
        ", constantName='" + constantName + '\'' +
        '}';
  }
}